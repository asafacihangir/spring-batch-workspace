package com.phoenix.springbatch.service;

import com.phoenix.springbatch.utils.FileOperationUtils;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UploadWatcher {

  private final Anonymizer anonymizer;
  private final String UPLOAD_DIRECTORY =
      FileOperationUtils.getWorkDirSubDirectory("public/upload");

  public UploadWatcher(Anonymizer anonymizer) {
    this.anonymizer = anonymizer;
  }

  @EventListener(value = ApplicationReadyEvent.class)
  public void watchAfterApplicationStarted() throws Exception {
    triggerJobForFilesAlreadyInUploadDirectory();
    createWatcherForNewFilesUploaded();
  }

  private void triggerJobForFilesAlreadyInUploadDirectory() {
    FileUtils.listFiles(new File(UPLOAD_DIRECTORY), new String[]{"json"}, false)
        .forEach(anonymizer::runAnonymizationJob);
  }

  private void createWatcherForNewFilesUploaded() throws Exception {
    FileAlterationObserver observer = new FileAlterationObserver(UPLOAD_DIRECTORY);
    FileAlterationMonitor monitor = new FileAlterationMonitor(5_000);
    observer.addListener(new FileAlterationListenerAdaptor() {
      @Override
      public void onFileCreate(File file) {
        anonymizer.runAnonymizationJob(file);
      }
    });
    monitor.addObserver(observer);
    monitor.start();
  }

}
