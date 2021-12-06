package ru.alarh.downloader.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alarh.downloader.controller.dto.MetaResult;
import ru.alarh.downloader.service.record.CameraRecordService;
import ru.alarh.downloader.service.record.dto.SearchResultObject;

@RestController
@RequestMapping(value = "/record")
@RequiredArgsConstructor
public class CameraRecordController {

    private final CameraRecordService cameraRecordService;

    @GetMapping(value = "/search")
    ResponseEntity<MetaResult<SearchResultObject>> search() {
        MetaResult<SearchResultObject> result = cameraRecordService.searchByAll();
        return ResponseEntity.ok(result);
    }

}