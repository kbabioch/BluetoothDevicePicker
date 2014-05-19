# BluetoothDevicePicker

![BluetoothDevicePicker Screenshot][0]

This is an Android library, which provides means for users to pick a specific
Bluetooth device. This basic functionality is common to all applications
dealing with Bluetooth and is implemented by various applications in different
ways.

This is an attempt to to provide an universal device picking dialog originally
developed for the [WordclockRemote][1] project. It can easily be added to any
Android project and is designed to be user friendly. Its core concept is
inspired by [Gnome Bluetooth][2] to some extent. Remote devices are constantly
scanned for without the need for users to keep track of it. Most details are
hidden from the user, e.g. only names of the remote devices are shown and no
difference is made between paired and unpaired devices. Both are shown in the
same list to the user. It is the responsibility of the application to handle
the chosen device appropriately, i.e. bonding and connecting to it.

## BUILDING

Simply import the project into Eclipse. Note that you might need to import
`libs/appcompat_v7` as another project, if this particular support library
is not part of your workspace already.

## USAGE

- Add this library as a dependency to your actual project

- Make sure your application has the required permissions (AndroidManifest.xml):

    ```XML
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    ```

- Use the following to show the dialog to the user:

    ```Java
    BluetoothDevicePickerDialog dialog = new BluetoothDevicePickerDialog();
    dialog.show(getFragmentManager(), null);
    ```

- Make sure the hosting activity implements `BluetoothDevicePickedListener`

- You can handle the picked Bluetooth device within `bluetoothDevicePicked()`:

    ```Java
    @Override
    public void bluetoothDevicePicked(BluetoothDevice device)
    {

        // Handle picked Bluetooth device, e.g. try to establish a connection

    }
    ```

## DONATIONS

[![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png "Flattr This!")](https://flattr.com/submit/auto?user_id=johnpatcher&url=https://github.com/johnpatcher/BluetoothDevicePicker)

[![PayPal donation](https://www.paypal.com/en_US/i/btn/btn_donate_LG.gif "PayPal")](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=karol%40babioch%2ede&lc=DE&item_name=BluetoothDevicePicker&no_note=0&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest)

Bitcoin: `1KcbNpD8Uqt6RXvMVtRGsYJFSiSAcHkhZB`

## LICENSE

[![GNU GPLv3](http://www.gnu.org/graphics/gplv3-127x51.png "GNU GPLv3")](http://www.gnu.org/licenses/gpl.html)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

[0]: https://raw.githubusercontent.com/johnpatcher/BluetoothDevicePicker/master/screenshots/dialog_bluetooth1.png "Screenshot"
[1]: https://github.com/Wordclock/remote
[2]: https://git.gnome.org/browse/gnome-bluetooth/

