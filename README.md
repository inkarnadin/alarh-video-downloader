# Remote video file downloader
Service for downloading video files from a remote server.

# Prepare
To configure, specify the following parameters via `application.yml`: 
* **alarh.source.path**: path to source of targets - plain file.  
  Format *ip:path:login:password:name*.  
  Examples: 
  * 8.1.2.33::admin:qwerty12345:test_camera2
  * 8.1.2.34::admin:qwerty12345:test_camera2
* **alarh.search.startDate** date of start search.  
  Format *YYYY-MM-DDTHH:MI:SSZ*
* **alarh.search.endDate**: date of end search.  
  Format *YYYY-MM-DDTHH:MI:SSZ*
* **alarh.search.resultLimit**: limit of search result list.
* **alarh.search.startPosition**: start position of search.

# Usage
For searching by target list from source file do `GET /record/search`. The results of a successful search will be cached.  
For downloading by all cached playbacks do `GET /record/download`. The downloading results will be save to result directory (by default `/result`). 

Downloading is performed only by previously found links that were saved in the cache 
