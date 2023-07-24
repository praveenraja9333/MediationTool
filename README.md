
**Note:** - This software is a demonstration and for educational purposes only.

This is the POC for the Highly concurrent, low GC Mediation tool. currently, for the POC only SFTP is considered.

**create package**
mvn package

**install**
zip will be available at the target/ directory. copy and unzip to the current location.

**run**
Though the app can run on both Windows, Mac and Linux, provided run.sh script work  on Linux and Mac 

**NodeConfig **:- (Example:- sample.json)\
Name : a unique name for an entire application(Please don’t repeat)\
srcDirectory :- Source directory from where the file needs to be fetched\
dstDirectory :- destination directory where the file will be stored\
fileregex :- filename pattern(Optional)



**Jobconfig:** - (Example: - refer sample.txt)\
ID: Any numeric value (can be redundant)\
Username: SFTP username\
destinationIP: SFTP server hostname or IP\
port: SFTP port (default to 22)\
protocol:sftp\
installnode :-  installedNode (such as LTECSS*)\
cron :- it cron expression for the job trigger\
sshpemkey:- This app won’t support password, creating a password-less SSH is a pre-requisite (use private key PEM format)



**Running snippets:** -\
Once the content from the zip is extracted to the location(Please create an empty directory and extract it). run\
                Example:1 ./run.sh\
                Example:2 ./run.sh -maxThread 10\
                Example:3 ./run.sh -maxThread 10 -configFile  sample.txt

Connect to app admin by running ./runClient.sh\
This client should  run in localhost to the server only\
1:- install Nodes\
2:- Show scheduled jobs\
3:- Show installed Nodes\
4:- Show installed Jobs\
5:- Remove Node( This will impact the current job schedule)\
6:- Remove Job ( This will impact the current job schedule)\
7:- reLoadJob ( This will impact the current job schedule)\
0:- exit\
Choose 1 or 2:\
You can do things as listed above\
1:- install Nodes\
Please provide the JSON file or the directory or type exit :/home/praveen/LTENODE1_3.json\
FileName /home/praveen/LTENODE1_3.json\
Success\
2: reLoadJob\
Choose 1 or 2:2\
[jobName] : 1:192.168.56.11:22:LTECSS3_2:0 0/6 * * * ? [groupName] : group1 - Mon Jul 24 06:24:00 EDT 2023


**webStateEndpoints** (Relaying the state information)\
"/api/v1/getInstalledNodes"\
"/api/v1/getScheduledJobs"\
"/api/v1/getScheduledJobs"
