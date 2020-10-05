# Bicycles Database
 
## Description
This project lets you manage a database of various types of bicycles. The bicycles have these attributes:
1. Type (mountain_bike, race_bike, or street_bike)
2. Number of gears (4 – 10, in increments of 1)
3. Wheel base (36 – 60 inches, in increments of 6 inches)
4. Height (1, 2, 3, 4)
5. Color (steel, red, blue, black)
6. Construction Material (carbon, steel, aluminium)
 
There are also three commands that you can run. Please note that Windows users will need to change `:` to `;` and the jdbc driver location in the classpath
1. AddBikes - Adds a bike into the table. All values must be specified.
   - Example: `java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar AddBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel`
2. RemoveBikes - Removes ALL bikes matching the values specified
   - Example: `java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar RemoveBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel`
3. ShowBikes - Shows all bikes matching the values specified, if the last attribute does not have a value, that will be the sort key
   - Example: `java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar ShowBikes -type mountain_bike -gear 5 -wheelbase`
           - Since `wheelbase` attribute does not have a value, that is the sort key
  
There is a one TestMyBicycles command that runs a simple test on all three commands above
 
This project database backend is mariadb. The code has been tested using this backend. There exists a mysql workbench package for my distro but this has not been tested. If there is time later, I will spin up a vm and install it there to see if my code works. But since we are presenting it in office hours, priorities for this are low.
 
mariadb is installed in a docker container. Run the shell script `docker-mariadb.sh` to shut-down and remove any previous instances, delete the previous database, start a fresh and new instance, and run a python script: `create-db.py` that creates the database tables and populates them properly.
 
Three key files: `tmp-db-ip`, `db-root-passwd`, and `db-data-dump.sql` are included in this project (however not committed into git). `tmp-db-ip` stores the ip address of the docker container that is running mariadb. `db-root-passwd` contains the root password of the database. Since this is not a production database, I will not be creating locked down users. `db-data-dump.sql` is an SQL dump of the entire database as requested by the assignment prompt. `mysqldump -u root -p$(cat ./db-root-passwd) -h $(cat ./tmp-db-ip) Bicycles > db-data-dump.sql` can be run to redump the database.
 
## How to use
- Compile with `javac *.java`
- Update/Edit your classpath to include the mariadb jdbc driver
   - If on Arch Linux, AUR package is: `yay -S mariadb-jdbc`
- Run with commands above or `java TestMyBicycles`
   - If on Arch Linux, AUR package installs driver in `/usr` path. Use this command: `java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar TestMyBicycles`
 
## Classes
There are 2 main sets of classes. The backend SQL and helper classes and the frontend command classes. The frontend command classes have a main function that can be run as stated in the description above
- Front End classes
   - AddBikes - Add bikes to the database
   - RemoveBikes - Removed bikes from the database
   - ShowBikes - Queries the database for bikes
   - TestMyBicycles - A simple linear test of all 3 commands. Check the "how to use" section for command.
- Back End classes
   - SQLOperation - A parent class that stores the connection to the database and printouts results from child class inquires.
   - InsertRow - Inserts a row into the database
   - DeleteRow - Deletes multiple rows from a database. Note that this command deletes ALL matching rows. `DELETE TOP(1) FROM` can be added to only restrict deleting one, or command line args can be extended to accept a number of rows to delete. Promt unclear.
   - SearchTable - Searches the table for matching rows based on the parameters passed in
   - BikesUtil - A class with static member utility functions. Helps parse arguments and files for various uses.
   - TableAttributes - A simple POD class to keep table attributes together and prevent function parameters from getting too long.
 
## Output Analysis
Note that the classpath was omitted from the commands for brevity.
- ShowBikesOutput.txt - `java ShowBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material`
   - Search the database for mountain bikes with 5 gears, a wheelbase of 36in, a height of 2, and in the color blue. Sort the list by material. The returned list has 3 entries, one for each material type.
- AddBikesOutput.txt - `java AddBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel`
   - Adds a bike with the supplied parameters. Since the database already contains bikes with every possible attribute, there is now two of them in the database
- RemoveBikesOutput.txt - `java RemoveBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel`
   - Removes all bikes with the given attributes, since they are both removed, there is no output
- TestMyBicyclesOutput.txt - `java TestMyBicycles`
   - Searches for a set of bicycles with height as the sort key, then inserts a bicycle. Now there are two of them, then remove both of the rows, then re add one of them into the database. Now, do the same search but sort by wheelbase.

