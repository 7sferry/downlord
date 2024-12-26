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
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class DownlordController{

	private final AppProperties appProperties;

	@GetMapping("/")
	public String fileList(@RequestParam(defaultValue = "") String path, Model model) throws FileNotFoundException{
		if(File.separatorChar == '\\'){
			path = path.replace('/', File.separatorChar);
		}
		String rootPath = appProperties.getRootPath();
		Path systemPath = Paths.get(rootPath + File.separatorChar + path).normalize();
		File[] files = systemPath.toFile().listFiles();
		if(!systemPath.startsWith(rootPath) || files == null){
			throw new FileNotFoundException(path);
		}
		Set<FileDto> fileDtos = getFileDtos(rootPath, files);
		model.addAttribute("files", fileDtos);
		String upperPath = getUpperPath(rootPath, systemPath);
		model.addAttribute("upperPath", upperPath);
		return "FileList";
	}

	private String getUpperPath(String rootPath, Path systemPath){
		Path parent = systemPath.getParent();
		String pathValue = parent != null ? Paths.get(rootPath).relativize(parent).toString() : null;
		return pathValue == null || pathValue.startsWith("..") ? "" : "?path=" + decorateSlash(pathValue);
	}

	private String decorateSlash(String pathValue){
		return File.separatorChar == '\\' ? pathValue.replace(File.separatorChar, '/') : pathValue;
	}

	private Set<FileDto> getFileDtos(String rootPath, File[] listFiles){
		return Stream.of(listFiles)
				.filter(File::canRead)
				.map(file -> getBuild(rootPath, file))
				.collect(Collectors.toCollection(() ->
						new TreeSet<>(Comparator.comparing(FileDto::isDirectory).reversed()
								.thenComparing(FileDto::getName, String.CASE_INSENSITIVE_ORDER))));
	}

	private FileDto getBuild(String rootPath, File file){
		Path path = file.toPath();
		Path root = Paths.get(rootPath);
		Path relativize = root.relativize(path);
		return FileDto.builder()
				.directory(file.isDirectory())
				.name(file.getName())
				.path((file.isDirectory() ? "?path=" : "dl?path=") + decorateSlash(relativize.toString()))
				.build();
	}

	@GetMapping("dl")
	public ResponseEntity<FileSystemResource> download(@RequestParam(defaultValue = "") String path) throws FileNotFoundException{
		String rootPath = appProperties.getRootPath();
		FileSystemResource resource = new FileSystemResource(rootPath + File.separatorChar + path);
		File file = resource.getFile();
		if(!file.isFile() || !file.exists() || !file.toPath().normalize().startsWith(rootPath)){
			throw new FileNotFoundException(path);
		}
		HttpHeaders headers = getHeaders(resource, file);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	private static HttpHeaders getHeaders(FileSystemResource resource, File file){
		MediaType mediaType = MediaTypeFactory
				.getMediaType(resource)
				.orElse(MediaType.APPLICATION_OCTET_STREAM);
		ContentDisposition disposition = ContentDisposition
				.attachment()
				.filename(file.getName())
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.setContentDisposition(disposition);
		return headers;
	}

}
