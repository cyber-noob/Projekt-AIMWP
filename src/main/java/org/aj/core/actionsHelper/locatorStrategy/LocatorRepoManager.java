package org.aj.core.actionsHelper.locatorStrategy;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.aj.core.Exceptions.MissingLocatorFileException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
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

    String locatorsResourceDir = System.getProperty("user.dir") + "/src/main/java/org/aj/application/locators/";

    //Hashmap of <PageName, JsonPath>
    public static HashMap<String, DocumentContext> locatorsCache = new HashMap<>();

    public void loadLocators() throws Exception {
        Set<File> locatorFiles = verifyIntegrity();

        for (File locatorFile : locatorFiles) {
            String pageName = locatorFile.getName()
                    .replace(".json", "");
            DocumentContext documentContext = JsonPath.parse(locatorFile);
            locatorsCache.put(pageName, documentContext);
        }
    }

    private Set<File> verifyIntegrity() throws Exception {
        Set<String> invokedClasses = getInvokedClasses();
        Set<String> classNames = new HashSet<>();
        for (String invokedClass : invokedClasses) {
            String[] splits = invokedClass.split("\\.");
            classNames.add(splits[splits.length - 1]);
        }

        Set<File> locatorFiles = getAllJsonFiles();

        Set<String> locatorFileClassNames = locatorFiles.stream()
                .map(File::getName)
                .map(fileName -> fileName.replace(".json", ""))
                .collect(Collectors.toSet());

        if(locatorFileClassNames.containsAll(classNames))
            return locatorFiles;
        else {
            locatorFileClassNames.removeAll(classNames);
            throw new MissingLocatorFileException("The following locator classes are missing that are expected for current automation run: " + locatorFileClassNames);
        }
    }

    private Set<File> getAllJsonFiles() throws Exception {
        List<Path> paths = Files.walk(Paths.get(locatorsResourceDir))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .toList();

        return paths.stream()
                .map(path -> new File(path.toUri()))
                .collect(Collectors.toSet());
    }
}
