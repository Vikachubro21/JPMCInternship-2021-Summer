# JPMCInternship-2021-Summer

FinalPackage contains the final package, with all of its related information.
source contains all of our source code.
If you would like to modify the jar, please follow the following steps
1. Create a new Maven Project in Intellij.
2. Replace the pom.xml with the one in the source folder and resolve dependencies.
3. Add our java files to the project.
4. In order to build the jar, add a build artifact, and then build the artifact. More information can be found on JetBrains.
If you have any questions, do not hesitate to contact me at vikas.thoutam@gmail.com
=======

With this program, you will be able to get information about the cluster(s) and table(s) that you have stored in Cassandra at the point of running. 


Before doing anything with the application, *RUN CASSANDRA* (either by using Command Prompt in Cassandra's location in File Explorer for Windows OS or through Terminal for Mac/Linux OS) or you will run into several errors trying to run methods from an application that isn't running.

If you are running this application on a device using Mac OS or Linux OS:
	After opening Cassandra, you will need to run the runner.sh Shell file through your Terminal by cding to the FinalPackage directory and typing "sudo bash runner.sh".
	If prompted for a password, the password to be given will be the Linux/Mac user's password that they use to login to their device. 

If you are running this application on a device using Windows OS:
	After opening Cassandra, you will need to run the clusterInfo.bat, tableStats.bat, and runner.bat Batch files by simply double clicking them in the directory.
	New files will be created, ignore them as nessesary

After performing one of the above steps, proceed with the following:
	To pull up the UI, go to terminal(Mac/Linux) or command prompt(Windows) and cd to the FinalPackage directory.
	then type the command "npm install serve"
	and then "serve -s build"
	it will state that a static server has been created at the address "localhost:5000"
	Now visit this address on your browser by simply opening a tab and either pasting or typing out the address.
	Once in the UI, direct your attention the the Choose File button on the left. 
	Click on the button, and within the FinalPackage Directory, select the file named "table_data.csv"
	A table should appear with your table statistics.
	Next direct your attention the the Choose File button on the right.
	Click on this button, and select the file in the FinalPackage Directort named "cluster_data.txt"

