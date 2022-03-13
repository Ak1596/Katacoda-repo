package com.example.drools.rule.engine.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

	private static final Logger logger = LoggerFactory.getLogger("RuleService");

	@Value("${rules.repo.url}")
	private String rulesRepoUrl;

	@Value("${rules.clone.base.url}")
	private String rulesUploadPath;

	private Git repository;
	static String id;

	public File createDirectory() throws IOException {
		Path directory = Files.createDirectories(Paths.get(rulesUploadPath));
		return directory.toFile();
	}

	public void cloneRepositry() {

		logger.info("Entering Clone Repositry Method");

		try {
			repository = Git.cloneRepository().setURI(rulesRepoUrl).setDirectory(createDirectory()).call();
			logger.info("Cloning Repositry Sucessful");

		} catch (GitAPIException | IOException e) {
			logger.error("Error occured during cloning remote reposistory : {}", e.getMessage());
		}

	}

	public void pull() {

		try {

			logger.info("Entering git pull request method");

			String commitId = "67efbe511278b5816b38b30ec60bd24f0b03a17e";
			System.out.println("Last commit id : " + commitId);

			LogCommand command = repository.log().setMaxCount(1);

			Iterable<RevCommit> logs = command.call();

			for (RevCommit revCommit : logs) {
				id = revCommit.getName();
				logger.info(getConventionalCommitMessage(revCommit));

				if (id != null && !id.equals(commitId)) {
					commitId = id.toString();

					PullResult result = repository.pull().call();
					if (result.isSuccessful()) {
						logger.info("Pulling latest commit from repository : {}", result.getFetchResult());
					}

				} else {
					logger.info("No new commit present in repository : {}");
				}

			}

		} catch (GitAPIException e) {

			logger.error("Error occured : {}", e.getMessage());

		}

	}

	private static String getConventionalCommitMessage(RevCommit commit) {
		StringBuilder stringBuilder = new StringBuilder();

		// Prepare the pieces
		final String justTheAuthorNoTime = commit.getAuthorIdent().toExternalString().split(">")[0] + ">";
		final Instant commitInstant = Instant.ofEpochSecond(commit.getCommitTime());
		final ZoneId zoneId = commit.getAuthorIdent().getTimeZone().toZoneId();
		final ZonedDateTime authorDateTime = ZonedDateTime.ofInstant(commitInstant, zoneId);
		final String gitDateTimeFormatString = "EEE MMM dd HH:mm:ss yyyy Z";
		final String formattedDate = authorDateTime.format(DateTimeFormatter.ofPattern(gitDateTimeFormatString));
		final String tabbedCommitMessage = Arrays.stream(commit.getFullMessage().split("\\r?\\n")) // split it up by
																									// line
				.map(s -> "\t" + s + "\n") // add a tab on each line
				.collect(Collectors.joining()); // put it back together

		// Put pieces together
		stringBuilder.append("commit ").append(commit.getName()).append("\n").append("Author:\t")
				.append(justTheAuthorNoTime).append("\n").append("Date:\t").append(formattedDate).append("\n\n")
				.append(tabbedCommitMessage);

		return stringBuilder.toString();
	}

}
