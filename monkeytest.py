# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
#from time
#import timeit
import datetime

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
device.installPackage('myproject/bin/MyApplication.apk')

# sets a variable with the package's internal name
package = 'com.example.listviewsample'

# sets a variable with the name of an Activity in the package
activity = 'com.example.android.listviewsample.ListViewSample'

# sets the name of the component to start
runComponent = package + '/' + activity

# Runs the component
device.startActivity(component=runComponent)

# Presses the Menu button
#device.press('KEYCODE_MENU','DOWN_AND_UP')


#for i in range(0,4):
#	device.drag((240,600), (240, 200), 1.0, 30)

#time.sleep(3)

#for i in range(0,4):
#	device.drag((240,200), (240, 600), 1.0, 30)


device.drag((240,600), (240, 200), 1.0, 10)
device.drag((240,600), (240, 200), 2.0, 10)
device.drag((240,600), (240, 200), 3.0, 10)
device.drag((240,600), (240, 200), 4.0, 10)

device.drag((240,200), (240, 600), 1.0, 10)
device.drag((240,200), (240, 600), 2.0, 10)
device.drag((240,200), (240, 600), 3.0, 10)
device.drag((240,200), (240, 600), 4.0, 10)

'''
MonkeyRunner.sleep(3)


def nomove():
	for i in range(4):
		device.drag((240,200), (240, 200), 1.0, 10)
	for i in range(4):
		device.drag((240,200), (240, 200), 1.0, 10)

def move():
	for i in range(4):
		device.drag((240,600), (240, 200), 1.0, 10)
	for i in range(4):
		device.drag((240,200), (240, 600), 1.0, 10)
'''
'''
t = timeit.Timer(nomove)
t = timeit.Timer(nomove)
print "Nomove = ", t.timeit()
print "Move = ", t.timeit()
'''
'''
start = datetime.datetime.now()
for i in range(4):
	device.drag((240,200), (240, 200), 1.0, 10)
for i in range(4):
		device.drag((240,200), (240, 200), 1.0, 10)
end = datetime.datetime.now()
print "*** No Move ***", end-start
'''
'''
#MonkeyRunner.sleep(3)

#start = datetime.datetime.now()
for i in range(100):
	device.drag((240,600), (240, 200), 1.0, 10)
for i in range(4):
	device.drag((240,200), (240, 600), 1.0, 10)
#end = datetime.datetime.now()
#print "*** Move ***", end-start
'''


'''
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)
device.drag((240,200), (240, 200), 1.0, 10)

device.drag((240,600), (240, 200), 1.0, 10)
device.drag((240,600), (240, 200), 1.0, 10)
device.drag((240,600), (240, 200), 1.0, 10)



device.drag((240,600), (240, 200), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,600), (240, 200), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,600), (240, 200), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,600), (240, 200), 1.0, 10)
MonkeyRunner.sleep(3)

device.drag((240,200), (240, 600), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,200), (240, 600), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,200), (240, 600), 1.0, 10)
MonkeyRunner.sleep(3)
device.drag((240,200), (240, 600), 1.0, 10)
MonkeyRunner.sleep(3)
'''

# Takes a screenshot
#result = device.takeSnapShot()

# Writes the screenshot to a file
#result.writeToFile('myproject/shot1.png','png')

