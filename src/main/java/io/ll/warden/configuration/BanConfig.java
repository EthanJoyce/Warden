package io.ll.warden.configuration;

import org.bukkit.event.Listener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import io.ll.warden.Warden;
import io.ll.warden.heuristics.BanReport;
import io.ll.warden.heuristics.CheckFailedReport;

/**
 * Creator: LordLambda
 * Date: 3/28/2015
 * Project: Warden
 * Usage: A ban config
 */
public class BanConfig extends Config implements Listener {

  private List<BanReport> bans;
  private List<CheckFailedReport> checks;
  private String name;
  private String prettyName;
  private File file;

  public BanConfig(String name) throws IOException {
    if(!name.contains(".")) {
      prettyName = name;
      name = prettyName + ".bc";
      load(new File(name));
    }else {
      prettyName = name.substring(0, name.indexOf('.'));
      this.name = name;
      load(new File(name));
    }
  }

  @Override
  protected void load(File file) throws IOException {
    if(!file.exists()) {
      file.createNewFile();
      this.file = file;

      return;
    }
    this.file = file;

    FileInputStream fis = new FileInputStream(file);
    byte[] bFile = new byte[(int) file.length()];
    fis.read(bFile);
    fis.close();
    byte[] decomp = ConfigManager.get().decompress(bFile);
    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decomp)));
    String line;
    while((line = br.readLine()) != null) {
      String[] split = line.split(",");
      if(split[0].equalsIgnoreCase("BAN")) {

      }else if(split[0].equalsIgnoreCase("CHECK")) {

      }
    }
  }

  @Override
  public String getFullName() {
    return name;
  }

  @Override
  public String getPrettyName() {
    return prettyName;
  }

  @Override
  protected void onShutdown() {
    try {
      FileOutputStream fos = new FileOutputStream(file);

    }catch(IOException e1) {
      Warden.get().log("Failed to load file...");
      e1.printStackTrace();
    }
  }
}
