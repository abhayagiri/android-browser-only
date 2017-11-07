Log in to the device via `adb shell` and execute the following:

```sh
adb shell dpm set-device-owner org.abhayagiri.browseronly/.DeviceAdmin
```

If you get the following error:

```
java.lang.IllegalStateException: Not allowed to set the device owner because there are already several users on the device
```

Then, on the device, go to Settings/Accounts and delete the current account. Try the `dpm set-device-owner` command again. You can add the account afterwards.

If you need to stop the task, execute the following via a connected PC:

```
adb shell am force-stop org.abhayagiri.browseronly
```
