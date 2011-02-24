# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
device.installPackage('myproject/bin/MyApplication.apk')

# Runs an activity in the application
device.startActivity(component='com.android.gl2jni')

# Presses the Menu button
#device.press('KEYCODE_MENU','DOWN_AND_UP')
#device.drag((10,100), (10, 600), 1, 100)

for i in range(100):
	device.touch(100,100,'DOWN_AND_UP') 
	MonkeyRunner.sleep(5.0)

# Takes a screenshot
#result = device.takeSnapShot

# Writes the screenshot to a file
#result.writeToFile('myproject/shot1.png','png')

