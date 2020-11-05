import { WebPlugin } from '@capacitor/core';
import { SunmiPrinterPlugin } from './definitions';

export class SunmiPrinterWeb extends WebPlugin implements SunmiPrinterPlugin {
  constructor() {
    super({
      name: 'SunmiPrinter',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async discoverPrinters(): Promise<{ results: any[] }> {
    return {
      results: [{
        name: 'Dummy',
        address: '123456'
      }]
    };
  }

  async connectPrinter(address: string): Promise<{ results: boolean }> {
    console.log(address);
    return {
      results: true
    };
  }

  async disconnectPrinter(): Promise<{ results: boolean }> {
    return {
      results: true
    };
  }

  async printString(contents: string, is_bold: boolean, is_underline: boolean): Promise<{ results: boolean }> {
    console.log("contents");
    console.log(contents);

    console.log("is_bold");
    console.log(is_bold);

    console.log("is_underline");
    console.log(is_underline);

    return {
      results: true
    };
  }

  async printBarcode(barcode: string): Promise<{ results: boolean }> {
    console.log("barcode");
    console.log(barcode);

    return {
      results: true
    };
  }

  async printCommand(command: string): Promise<{ results: boolean }> {
    console.log("command");
    console.log(command);

    return {
      results: true
    };
  }
}

const SunmiPrinter = new SunmiPrinterWeb();

export { SunmiPrinter };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SunmiPrinter);
