package com.example.drools.rule.engine.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

	private static final Logger logger = LoggerFactory.getLogger("RuleService");

	@Value("${rules.repo.url}")
	private String rulesRepoUrl;

	@Value("${rules.upload.base.url}")
	private String rulesUploadPath;

	public File createDirectory() throws IOException {
		Path directory = Files.createDirectories(Paths.get(rulesUploadPath));
		return directory.toFile();
	}

	public void cloneRepositry() {

		try {
			
			Git.cloneRepository().setURI(rulesRepoUrl).setDirectory(createDirectory()).call();
			
		} catch (GitAPIException | IOException e) {

			logger.error("Error occured during cloning remote reposistory : {}", e.getMessage());

		}

	}

}
