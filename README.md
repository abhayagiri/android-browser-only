# Browser Only

A simple Android app that pins a browser to a URL.

See the [Android Developers page](https://developer.android.com/work/cosu.html) For more information on screen pinning and `LockTask`.

## `dpm set-device-owner`

**This is no longer used as we're currently just using simple screen pinning. This is left here for reference.**

Log in to the device via `adb shell` and execute the following:

```sh
adb shell dpm set-device-owner org.abhayagiri.browseronly/.DeviceAdmin
```

If you get the following error:

```
java.lang.IllegalStateException: Not allowed to set the device owner because there are already several users on the device
```

Then, on the device, go to Settings/Accounts and delete the current account. Try the `dpm set-device-owner` command again. You can add an account afterwards but it still permanently sets the device owner.

If you need to stop the task, execute the following via a connected PC:

```
adb shell am force-stop org.abhayagiri.browseronly
```
