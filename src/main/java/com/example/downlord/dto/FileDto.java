/************************
 * Made by [MR Ferry™]  *
 * on December 2022     *
 ************************/

package com.example.downlord.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FileDto{
	String name;
	boolean directory;
	String path;
}
