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

  async discoverPrinters(filter: string): Promise<{ results: any[] }> {
    console.log('filter: ', filter);
    return {
      results: [{
        firstName: 'Dummy',
        lastName: 'Entry',
        telephone: '123456'
      }]
    };
  }
}

const SunmiPrinter = new SunmiPrinterWeb();

export { SunmiPrinter };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SunmiPrinter);
