/************************
 * Made by [MR Ferry™]  *
 * on December 2022     *
 ************************/

package com.example.downlord.dto;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Builder
@Value
public class FileDto{
	String name;
	ZonedDateTime dateCreated;
	ZonedDateTime dateModified;
	boolean directory;
	String path;
}
