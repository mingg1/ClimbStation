# ClimbStation

Android application for controlling indoor climbing machines of ClimbStation.

## Requirements
minimum API Level: Android 7.0 Nougat (SDK 24)

## Screenshots
 <img src=https://user-images.githubusercontent.com/61379336/146570362-b086e879-bd47-40d7-b313-f3eca5e3d04e.png  width="310" height="600"/>  <img src=https://user-images.githubusercontent.com/61379336/146574119-f35b7448-c1f1-4ae6-8ae1-660d396175d9.png  width="310" height="600"/> <img src=https://user-images.githubusercontent.com/61379336/146570427-72b53dc7-3255-4405-9825-876b6f59693d.png  width="310" height="600"/>
 <img src=https://user-images.githubusercontent.com/61379336/146570245-1d09dbce-5ae9-4917-85e4-5167b00dc813.png  width="310" height="600"/>  <img src=https://user-images.githubusercontent.com/61379336/146574161-0f769c5b-eba9-4215-9e77-3ad4768d2594.png  width="310" height="600"/> <img src=https://user-images.githubusercontent.com/61379336/146574150-35ead855-82d1-459b-adeb-e07a55ce701e.png  width="310" height="600"/>
 <img src=https://user-images.githubusercontent.com/61379336/146574205-8af308dd-57ee-4204-a7fa-d44ccd160612.png width="310" height="600"/> <img src=https://user-images.githubusercontent.com/61379336/146574249-99eaa5aa-50d5-44a9-99e4-3fed9f4831ba.png  width="310" height="600"/> <img src=https://user-images.githubusercontent.com/61379336/146575815-e22cae14-794c-44db-9766-d621127033e4.png  width="310" height="600"/>

## Usage
### Connect to the climbing machine
Before you open the application, you have to connect to the same network as the ClimbStation machines.
Open assets/climbstation.conf file, and change ip address of httpserverHost file to your own address.
Once connected, you will see a QR code reader to get the serial number attached to the machines. Scan the code, or you can also write down the serial number manually.

### Select climbing program
Navigate to the 'Climb' view, and you can swipe to the left and right to see two different views.

- #### Quick start
You can see the list of climbing programs in the first view. The list shows both predefined programs and custom profiles (programs). You can filter options to display predefined options, custom options, or both.
Click on one in the list to see the program's information, overall length, and angle range. The program will be started with average speed.

- #### Advanced settings
Swipe to the right to set more precise climbing options.
You can set the program, the length you want to climb, the speed of the machine, and the climb mode. Climbing mode is for the machine to repeat the same program, move to the next program or start a random program if you want to climb longer than the total length of the selected program.

### Climbing progress
On the climbing progress view, you can check duration, climbed length status, current angle of the machine, and your burnt calories.
Additionally, you can check the angle and duration length of the next step.
If necessary, pause and restart the machine.
When the set climbing program is complete, your phone vibrates to notify you that the climbing program has ended and displays the climbing status.

### Climb histories
Navigate to the 'Statistics' view, you can see a list of climbing that you have done in the newest order.

### Create your custom climbing profile (program)
Navigate to the 'Settings' view, and click "Create custom terrain profile" tab. You can name your profile, and add phases as many as you want.
You should set at least one phase with the required minimum length and angle in a valid range. When you save the profile, you can see the one in the list of programs from Climb view.

### Manage custom climbing profile (program)
Navigate to the 'Settings' view, and click "Manage custom terrain profile" tab. You can see a list of created custom profiles (program). Swipe to the left to delete a profile. Click to edit a profile. You can edit the name, already defined phase(s), or also add or remove phase(s).

### Set your body weight
Navigate to the 'Settings' view, and click "Set body weight" tab. Save your body weight to calculated burnt calories while climbing. The default value is 60kg.

## Developers
General Climbing Team (Minji Choi, Anne Pier Merkus, Anjan Shakya)
## License
GNU General Public License v3.0
