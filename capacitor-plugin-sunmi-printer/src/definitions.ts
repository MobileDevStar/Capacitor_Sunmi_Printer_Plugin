declare module '@capacitor/core' {
  interface PluginRegistry {
    SunmiPrinter: SunmiPrinterPlugin;
  }
}

export interface SunmiPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  discoverPrinters(): Promise<{results: any[]}>;
  connectPrinter(address: string): Promise<{results: boolean}>;
  disconnectPrinter(): Promise<{results: boolean}>;
  printString(contents: string, is_bold: boolean, is_underline: boolean): Promise<{results: boolean}>;
  printBarcode(barcode: string): Promise<{results: boolean}>;
  printCommand(command: string): Promise<{results: boolean}>;
}
