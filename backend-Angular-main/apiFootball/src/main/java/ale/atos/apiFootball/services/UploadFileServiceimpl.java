package ale.atos.apiFootball.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceimpl implements UploadFileService {

	private final Logger log = LoggerFactory.getLogger(UploadFileServiceimpl.class);
	private final static String DIRECTORIO_UPLOAD = "uploads";
	
	@Override
	public Resource load(String photoName) throws MalformedURLException {
		
		Path filePath = getPath(photoName);
		log.info(filePath.toString());
		
		Resource resource = new UrlResource(filePath.toUri());
		
		if (!resource.exists() && !resource.isReadable()) {
			
			filePath = Paths.get("src/main/resources/static/images").resolve("Default_user_icon.png").toAbsolutePath();
			resource = new UrlResource(filePath.toUri());
			log.error("Error no se pudo cargar la imagen " + photoName);
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
		
		Path filePath = getPath(fileName);
		log.info(filePath.toString());
		
		Files.copy(file.getInputStream(), filePath);
		
		return fileName;
	}

	@Override
	public Boolean delete(String photoName) {
		
		if (photoName != null && photoName.length() > 0) {
			
			Path lastPhotoPath = Paths.get("uploads").resolve(photoName).toAbsolutePath();
			File lastPhotoFile = lastPhotoPath.toFile();
			
			if (lastPhotoFile.exists() && lastPhotoFile.canRead()) {
				
				lastPhotoFile.delete();
				return true;
				
			}
		}
		return false;
	}

	@Override
	public Path getPath(String photoName) {
		return Paths.get(DIRECTORIO_UPLOAD).resolve(photoName).toAbsolutePath();
	}

}
