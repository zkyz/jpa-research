package kr.or.knia.config.multipart;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class UploadFile implements MultipartFile {
	private MultipartFile file;
	private File storedFile;
	
	public UploadFile() {}
	public UploadFile(File storedFile) {
		this.storedFile = storedFile;
	}
	public UploadFile(String filepath) {
		this.storedFile = new File(filepath);
	}
	
	public String getAbsolutePath() {
		if(storedFile != null) {
			return storedFile.getAbsolutePath();
		}
		
		return null;
	}

	//@Deprecated
	public InputStream getInputStream() throws IOException {
		//throw new UnsupportedOperationException("U must use to transferTo!!!");

		if(file != null) {
			return file.getInputStream();
		}
		else if(storedFile != null && storedFile.exists()) {
			return new FileInputStream(storedFile);
		}

		return null;
	}
	
	public String getName() {
		if(file != null) {
			return file.getName();
		}
		else if(storedFile != null) {
			return storedFile.getName();
		}
		
		return null;
	}

	public String getOriginalFilename() {
		if(file != null) {
			return file.getOriginalFilename();
		}
		else if(storedFile != null) {
			return storedFile.getName();
		}

		return null;
	}

	public String getContentType() {
		if(file != null) {
			return file.getContentType();
		}

		return null;
	}

	public boolean isEmpty() {
		if(file != null) {
			return file.isEmpty();
		}
		else if(storedFile != null) {
			return !storedFile.exists();
		}

		return true;
	}

	public long getSize() {
		if(file != null) {
			return file.getSize();
		}

		return -1;
	}

	public byte[] getBytes() throws IOException {
		if(file != null) {
			return file.getBytes();
		}

		return null;
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
		if(file != null) {
			file.transferTo(dest);
			storedFile = dest;
		}
		else if(storedFile != null) {
			dest = storedFile;
		}
	}
	
	
}
