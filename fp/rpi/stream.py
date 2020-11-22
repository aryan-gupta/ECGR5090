# import required modules
from flask import Flask, render_template, Response 
from imutils.video import VideoStream
import numpy as np
import cv2
import time
import imutils

class MotionDetector:
	def __init__(self, base, weight=0.25):
		self.weight = weight
		self.background = base.copy().astype("float")

	def update(self, image):
		cv2.accumulateWeighted(image, self.background, self.weight)

	def get_motion_bounding_box(self, image, threshold=25):
		# compute difference between new image and background
		delta = cv2.absdiff(self.background.astype("uint8"), image)
		thresh = cv2.threshold(delta, threshold, 255, cv2.THRESH_BINARY)[1]

		# remove thresholds that are too small (sensor noise)
		thresh = cv2.erode(thresh, None, iterations=2)
		thresh = cv2.dilate(thresh, None, iterations=2)

		# find the contors from the new image
		cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
		cnts = imutils.grab_contours(cnts)
		(minX, minY) = (np.inf, np.inf)
		(maxX, maxY) = (-np.inf, -np.inf)

		if len(cnts) == 0:
			return None

		for c in cnts:
			# compute the bounding box of the contour and use it to
			# update the minimum and maximum bounding box regions
			(x, y, w, h) = cv2.boundingRect(c)
			(minX, minY) = (min(minX, x), min(minY, y))
			(maxX, maxY) = (max(maxX, x + w), max(maxY, y + h))

		return (thresh, (minX, minY, maxX, maxY))

# Create Flask application
app = Flask(__name__) 

# start video camera and wait for it
# to initialize
vc = cv2.VideoCapture(0)
time.sleep(2.0)

if not vc.isOpened():
	print("Cannot open camera")
	exit()

ret, frame = vc.read()
if not ret:
	print("Can't receive frame (stream end?). Exiting ...")
	exit()

# read one frame and initialize the motion detector
# from this frame
frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
detector = MotionDetector(frame)

def gen(): 
	"""Video streaming generator function.""" 

	global vc

	while True: 
		rval, frame = vc.read()
		gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

		if not rval:
			print("Can't receive frame (stream end?). Exiting ...")
			break

		bounding_box = detector.get_motion_bounding_box(gray)
		detector.update(gray)

		if bounding_box is not None:
			(thresh, (minX, minY, maxX, maxY)) = bounding_box
			cv2.rectangle(frame, (minX, minY), (maxX, maxY), (0, 0, 255), 1)

		(flag, encodedImage) = cv2.imencode(".jpg", frame) 
		if not flag:
			continue

		yield (b'--frame\r\n' 
				b'Content-Type: image/jpeg\r\n\r\n' + bytearray(encodedImage) + b'\r\n') 


@app.route('/') 
def index(): 
	"""Video streaming .""" 
	return render_template('index.html') 

@app.route('/video_feed') 
def video_feed(): 
	"""Video streaming route. Put this in the src attribute of an img tag.""" 
	return Response(gen(), mimetype='multipart/x-mixed-replace; boundary=frame') 


app.run(host='0.0.0.0', port=8081, debug=False, threaded=True) 
vc.release()
