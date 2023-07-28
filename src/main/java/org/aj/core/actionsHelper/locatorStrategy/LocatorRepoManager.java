package org.aj.core.actionsHelper.locatorStrategy;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.aj.core.Exceptions.MissingLocatorFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.aj.core.listeners.CustomerTestListener.getInvokedClasses;

/**
 * Responsible for parsing
 * caching required locators lazily
 * Verifies if all the expected test class relevant locators file are present
 * Throws: Runtime LocatorNotSpecifiedException
 */
public class LocatorRepoManager {

    String locatorsResourceDir = "/locators";

    //Hashmap of <PageName, JsonPath>
    protected static HashMap<String, DocumentContext> locatorsCache = new HashMap<>();

    public void loadLocators() throws IOException, MissingLocatorFileException {
        Set<File> locatorFiles = verifyIntegrity();

        for (File locatorFile : locatorFiles) {
            String pageName = locatorFile.getName();
            DocumentContext documentContext = JsonPath.parse(locatorFile);
            locatorsCache.put(pageName, documentContext);
        }
    }

    private Set<File> verifyIntegrity() throws IOException, MissingLocatorFileException {
        Set<String> invokedClasses = getInvokedClasses();
        Set<File> locatorFiles = getAllJsonFiles();

        Set<String> locatorFileClassNames = locatorFiles.stream()
                .map(File::getName)
                .collect(Collectors.toSet());

        if(locatorFileClassNames.containsAll(invokedClasses))
            return locatorFiles;
        else {
            locatorFileClassNames.removeAll(invokedClasses);
            throw new MissingLocatorFileException("The following locator classes are missing that are expected for current automation run: " + locatorFileClassNames);
        }
    }

    private Set<File> getAllJsonFiles() throws IOException {
        List<Path> paths = Files.walk(Paths.get(locatorsResourceDir), 1)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().endsWith(".json"))
                .toList();

        return paths.stream()
                .map(path -> new File(path.toUri()))
                .collect(Collectors.toSet());
    }
}
