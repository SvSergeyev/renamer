package tech.sergeyev.renamer.service;

import tech.sergeyev.renamer.exception.FileRenameException;
import tech.sergeyev.renamer.exception.IdParserException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RenameService {
    private static final String TEMPLATE = "sout_number";

    public ServiceResult rename(List<File> mintrudFiles, List<File> expertFiles) {
        var messages = new ArrayList<String>();
        var result = new ServiceResult();
        try {
            var groupedExpertNames = groupFilesById(expertFiles);
            var groupedMintrudNames = groupFilesById(mintrudFiles);
            messages.addAll(assignExpertNamesToMintrud(groupedExpertNames, groupedMintrudNames));
        } catch (IdParserException | FileRenameException ex) {
            result.setStatus(ResultStatus.ERROR);
            messages.add(ex.getMessage());
        }
        result.getMessages().addAll(messages);
        return result;
    }

    private List<String> assignExpertNamesToMintrud(
        Map<Integer, List<File>> expertFilesMap,
        Map<Integer, List<File>> mintrudFilesMap) {
        var responseList = new ArrayList<String>();
        var usedExpertFiles = new ArrayList<File>();
        var mintrudFilesCount = new ArrayList<>(mintrudFilesMap.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList())).size();
        var renamedCount = 0;
        for (Map.Entry<Integer, List<File>> mintrud : mintrudFilesMap.entrySet()) {
            var id = mintrud.getKey();
            if (expertFilesMap.containsKey(id)) {
                var expertFiles = expertFilesMap.get(id);
                expertFiles.removeAll(usedExpertFiles);
                for (var mintrudFile : mintrud.getValue()) {
                    if (!expertFiles.isEmpty()) {
                        var expertFile = expertFiles.remove(0);
                        usedExpertFiles.add(expertFile);
                        renameFile(id, expertFile, mintrudFile);
                        renamedCount++;
                    }
                }
            } else {
                responseList.add("В списке файлов выписки не найден id=" + id);
            }
        }
        responseList.add("Получено файлов выписки: " + mintrudFilesCount +
            ", переименовано: " + renamedCount);
        return responseList;
    }

    private void renameFile(int id, File source, File target) {
        var targetFilename = target.getName();
        var formattedCurrentExpertFilename = source.getName()
            .replace(String.format("%d_", id), "")
            .replace(".sout", "");
        var newName = targetFilename.replace(TEMPLATE, formattedCurrentExpertFilename);
        var renamed = new File(target.getParent(), newName);
        if (!target.renameTo(renamed)) {
            throw new FileRenameException("Не удалось переименовать файл: " + target.getName());
        }
    }

    private Map<Integer, List<File>> groupFilesById(Collection<File> files) {
        return files.stream()
            .collect(Collectors.groupingBy(file -> findId(file.getName())));
    }

    private int findId(String text) {
        String regex = "\\d{5,}";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);

        int maxNumber = Integer.MIN_VALUE;

        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            if (number > maxNumber) {
                maxNumber = number;
            }
        }
        if (maxNumber == Integer.MIN_VALUE) {
            throw new IdParserException("Имя " + text + " не содержит чисел длиннее 5 знаков");
        }
        return maxNumber;
    }
}