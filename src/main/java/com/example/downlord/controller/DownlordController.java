/************************
 * Made by [MR Ferry™]  *
 * on December 2022     *
 ************************/

package com.example.downlord.controller;

import com.example.downlord.config.AppProperties;
import com.example.downlord.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class DownlordController{

	private static final String FILE_LIST = "FileList";
	private final AppProperties appProperties;

	@GetMapping("list")
	public String fileList(@RequestParam(defaultValue = "") String path, Model model){
		String rootPath = appProperties.getRootPath();
		Path systemPath = Paths.get(rootPath + File.separator + path);
		Path normalize = systemPath.normalize();
		File[] listFiles = systemPath.toFile().listFiles();
		if(!normalize.toString().startsWith(rootPath) || listFiles == null){
			model.addAttribute("upperPath", "list");
			model.addAttribute("error", "File or folder does not exist");
		} else {
			Set<FileDto> files = getFileDtos(rootPath, listFiles);
			model.addAttribute("files", files);
			String pathValue = systemPath.getParent().toString().replaceFirst(rootPath, "");
			model.addAttribute("upperPath", (pathValue.equals(rootPath) || pathValue.equals("/") ? "" : "list?path=" + pathValue));
		}
		return FILE_LIST;
	}

	private Set<FileDto> getFileDtos(String rootPath, File[] listFiles){
		return Stream.of(listFiles)
				.filter(File::canRead)
				.map(file -> FileDto.builder()
						.directory(file.isDirectory())
						.name(file.getName())
						.path((file.isDirectory() ? "list?path=" : "dl?path=") + file.getPath().replaceFirst(rootPath, ""))
						.build())
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FileDto::isDirectory).reversed().thenComparing(FileDto::getName))));
	}

	@GetMapping("dl")
	public Object dl(@RequestParam(defaultValue = "") String path, Model model){
		String rootPath = appProperties.getRootPath();
		FileSystemResource resource = new FileSystemResource(rootPath + File.separator + path);
		File file = resource.getFile();
		if(!file.isFile() || !file.exists() || !file.toPath().normalize().toString().startsWith(rootPath)){
			model.addAttribute("error", "File or folder does not exist");
			return FILE_LIST;
		}
		MediaType mediaType = MediaTypeFactory
				.getMediaType(resource)
				.orElse(MediaType.APPLICATION_OCTET_STREAM);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		ContentDisposition disposition = ContentDisposition
				.attachment()
				.filename(Objects.requireNonNull(resource.getFilename()))
				.build();
		headers.setContentDisposition(disposition);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

}
