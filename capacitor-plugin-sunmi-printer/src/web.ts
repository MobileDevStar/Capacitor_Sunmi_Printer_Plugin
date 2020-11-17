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
        name: 'Dummy1',
        address: '123456'
      },{
        name: 'Dummy2',
        address: '456789'
      }]
    };
  }

  async connectPrinter(options: {address: string}): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);
    return {
      results: true
    };
  }

  async disconnectPrinter(): Promise<{ results: boolean }> {
    return {
      results: true
    };
  }

  async printString( options: { contents: string, is_bold: boolean, is_underline: boolean } ): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);
    
    return {
      results: true
    };
  }

  async printBarcode(options: {barcode: string }): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);

    return {
      results: true
    };
  }

  async printCommand(options: {command: string}): Promise<{ results: boolean }> {
    console.log("options");
    console.log(options);

    return {
      results: true
    };
  }
}

const SunmiPrinter = new SunmiPrinterWeb();

export { SunmiPrinter };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SunmiPrinter);
