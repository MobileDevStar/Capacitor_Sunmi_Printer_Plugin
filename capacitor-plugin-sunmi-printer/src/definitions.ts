declare module '@capacitor/core' {
  interface PluginRegistry {
    SunmiPrinter: SunmiPrinterPlugin;
  }
}

export interface SunmiPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  discoverPrinters(filter: string): Promise<{results: any[]}>;
}
