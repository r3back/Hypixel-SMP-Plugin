package me.reb4ck.smp.factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.factory.OSFactory;
import me.reb4ck.smp.api.service.OSService;
import me.reb4ck.smp.base.service.os.LinuxService;

import java.io.File;
import java.io.IOException;

@Singleton
public final class OSFactoryImpl implements OSFactory {
    private final OSService osService;
    private final Injector injector;

    @Inject
    public OSFactoryImpl(Injector injector){
        this.injector = injector;
        this.osService = getOs();
    }

    @Override
    public OSService getOSService() {
        return osService;
    }

    private OSService getOs(){
        return injector.getInstance(LinuxService.class);
    }

    public void excuteCommand(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.isFile()){
            throw new IllegalArgumentException("The file " + filePath + " does not exist");
        }
        if(isLinux()){
            Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", filePath}, null);
        }else if(isWindows()){
            Runtime.getRuntime().exec("cmd /c start " + filePath);
        }
    }

    public boolean isLinux(){
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("linux") >= 0;
    }

    public boolean isWindows(){
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("windows") >= 0;
    }
}
