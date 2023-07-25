#!/bin/bash

## The following bash script configures your system to run app automation instantly without any manual configurations required from user
export HOME="$(getent passwd $SUDO_USER | cut -d: -f6)"

rcFile=$1
addOrUpdateToFile() {
  val="$(echo $val | sed 's_/_\\/_g')"

  if grep -q "^export $prop=" "$rcFile"; then
    sed -i "s/^export $prop=.*$/export $prop=$val/" "$rcFile" &&
    echo "[updated] export $prop=$val"
  else
    echo -e "export $prop=$val" >> "$rcFile"
    echo "[inserted] export $prop=$val"
  fi
}

echo "########################### Installing node ###########################"
apt update && apt install nodejs -y

echo "########################### Installing appium as global installation ###########################"
npm i -g appium

echo "########################### Installing webdriver ###########################"
npm i -g wd

echo "########################### Installing appium-doctor as global installation (Useful for config debug purposes) ###########################"
npm i -g appium-doctor

echo "########################### Setting up Java and env vars ###########################"
apt update && apt install default-jdk -y
#echo -e "export JAVA_HOME=$(readlink -f $(which java))" >> $HOME/.bashrc

prop="JAVA_HOME"
val="$(readlink -f $(which java))"
addOrUpdateToFile prop val

echo "########################### Setting up Android-sdk ###########################"
##Setup android sdk
apt update && apt install android-sdk -y

setupAndroidSdkPath () {
#  echo -e "export ANDROID_HOME=$variable" >> $HOME/.bashrc
  prop="ANDROID_HOME"
  val="$variable"
  addOrUpdateToFile prop val

#  echo -e "export PATH=$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$PATH" >> $HOME/.bashrc
  prop="PATH"
  val="$ANDROID_HOME/emulator:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH"
  addOrUpdateToFile prop val

  source $HOME/.bashrc
  PATH=$(echo $PATH)

#  echo -e "export PATH=$PATH:$ANDROID_HOME/platform-tools" >> $HOME/.bashrc

  echo $ANDROID_HOME
}

if [ -d "/usr/lib/android-sdk" ]
then
  echo "/usr/lib/android-sdk dir exists... Setting up env vars with the value"
  export variable="/usr/lib/android-sdk"
  setupAndroidSdkPath
elif [ -d "$HOME/Android/Sdk/" ]; then
  echo "$HOME/Android/Sdk/ dir exists... Setting up env vars with the value"
  export variable="$HOME/Android/Sdk/"
  setupAndroidSdkPath
elif [ -d "/Library/Android/sdk/" ]; then
  echo "/Library/Android/sdk/ dir exists... Setting up env vars with the value"
  export variable="/Library/Android/sdk/"
  setupAndroidSdkPath
else
  echo "!!!... Android sdk dir not found ....!!!"
fi

echo "########################### Setting up NODE_PATH ###########################"
##Setup NODE_PATH=path_to_node
#echo -e "export NODE_PATH=$(which node)" >> $HOME/.bashrc
prop="NODE_PATH"
val="$(which node)"
addOrUpdateToFile prop val

echo $NODE_PATH

echo "########################### Setting up APPIUM_PATH ###########################"
##Setup APPIUM_PATH=path_to_appium
#echo -e "export APPIUM_PATH=$(which appium)" >> $HOME/.bashrc
prop="APPIUM_PATH"
val="$(which appium)"
addOrUpdateToFile prop val

echo $APPIUM_PATH

echo "########################### Setting up PATH ###########################"
##Export PATH=/usr/local/bin:$PATH
#echo -e "export PATH=/usr/local/bin:$PATH" >> $HOME/.bashrc
#prop="PATH"
#val="/usr/local/bin:$PATH"
#addOrUpdateToFile prop val
#
#echo $PATH

#echo "########################### Sourcing current changes ###########################"
source $HOME/.bashrc


