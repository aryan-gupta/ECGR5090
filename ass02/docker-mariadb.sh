# some mysql dev is rolling in their sleep
# Start docker service
sudo systemctl start docker.service
# remove all data from previous run
docker stop ass02-db
docker rm ass02-db
sudo rm ./mysqldata -rf
mkdir mysqldata -p
# start new sql database instance
docker run -p "3206:3206" --name ass02-db -v /home/aryan/Projects/ECGR5090/ass02/mysqldata:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=$(cat ./db-root-passwd) -d mariadb:latest
# save ip address
docker inspect --format '{{ .NetworkSettings.IPAddress }}' ass02-db | tee ./tmp-db-ip
# sleep for 2 seconds for the database to populate
echo "waiting 30 seconds for mysql"
sleep 30
# populate database
python create-db.py
# start test connection
# suto-rehash allows tab autocompletion
mysql --auto-rehash -u root -p$(cat ./db-root-passwd) -h $(cat ./tmp-db-ip)