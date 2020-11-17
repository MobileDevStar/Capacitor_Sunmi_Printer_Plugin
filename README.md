## Capacitor_Sunmi_Printer_Plugin
This is a capacitor plugin to use sunmi printer

---

## Installation
1. Download or clone the source form Gitbub

2. Install package via npm from local of the source:

```
npm install --save ./capacitor-plugin-sunmi-printer
```

3. Require in your project

```javascript
import { Plugins } from '@capacitor/core';
import 'capacitor-plugin-sunmi-printer';        //for test in the web browser

const { SunmiPrinter } = Plugins;
```

4. Test print

```javascript
    printTest = async () => {
        const data = {
            contents : "This is a test print \n",
            is_bold: true,
            is_underline: true
        }

        const results = (await SunmiPrinter.printString(data)).results;
        return results;
    };
```

## Properties

### `discoverPrinters: () => array`

Discover bluetooth printers.

e.g.
```javascript
    discoverPrinters = async () => {
        const results = (await SunmiPrinter.discoverPrinters()).results;

        var printers: any[] = [];
        for (var i = 0; i< results.length; i++) {
            let printer = {
                "name": results[i].name,
                "address": results[i].address,
            }

            printers.push(printer);
        }

        return printers;
    };
```

---

### `connectPrinter?: (option) => boolean`

Connect to the printer by bluetooth address

e.g.
```javascript
    connectPrinter = async () => {
        const option = {
            address : "xxx xxx xxx"
        }

        const results = (await SunmiPrinter.connectPrinter(option)).results;
        return results;
    };
```

---

### `disconnectPrinter?: () => boolean`

Disconnect from the printer

e.g.
```javascript
    disconnectPrinter = async () => {
        const results = (await SunmiPrinter.disconnectPrinter()).results;
        return results;
    };
```

---

### `printString?: (options) => boolean`

Print string contents with bold and underline

e.g.
```javascript
    printTest = async () => {
        const options = {
            contents : "This is a test print \n",
            is_bold: true,
            is_underline: true
        }

        const results = (await SunmiPrinter.printString(options)).results;
        return results;
    };
```

---

### `printCommand?: (option) => boolean`

Send print command to the printer.

-   `align_left` - Left alignment.
-   `align_right` - Right alignment.
-   `align_center` - Center alignment.
-   `next_line` - Next line.

e.g.
```javascript
    printCommand = async () => {
        const option = {
            command : 'align_left'
        }
        const results = (await SunmiPrinter.printCommand(option)).results;
        return results;
    };
```

---

### `printBarcode?: (option) => boolean`

Print barcode

e.g.
```javascript
    printBarcode = async () => {
        const option = {
            barcode : '123xxxxx'
        }
        const results = (await SunmiPrinter.printBarcode(option)).results;
        return results;
    };
```

---