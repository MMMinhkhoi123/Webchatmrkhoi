package com.chatmrkhoi.chatmrkhoi.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.chatmrkhoi.chatmrkhoi.design.Signgleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.chatmrkhoi.chatmrkhoi.entity.Users_entity;
import com.chatmrkhoi.chatmrkhoi.entity.file_entity;
import com.chatmrkhoi.chatmrkhoi.reponse.datadetail_response;
import com.chatmrkhoi.chatmrkhoi.reponse.filedetail_reponse;
import com.chatmrkhoi.chatmrkhoi.reponsitory.User_repo;
import com.chatmrkhoi.chatmrkhoi.reponsitory.file_repo;
import com.chatmrkhoi.chatmrkhoi.reponsitory.mess_repo;
import com.chatmrkhoi.chatmrkhoi.request.file_reuqest;
import com.chatmrkhoi.chatmrkhoi.service.File_inter;

@Service
public class File_service implements File_inter {

	@Autowired
	file_repo file_repo;

	@Autowired
	User_repo user_repo;
	@Autowired
	mess_repo mess_repo;

	
	@Override
	public file_entity savefile(String namefile, String typefile, Long size) {
		file_entity data = file_entity.builder().namefile(namefile).typefile(typefile).size(size).status(false).build();
	  return file_repo.save(data);
	}
	String name ;
	Long id;
	
	
	// RETURN USER AUTHENCATED
	public Users_entity user_authe() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user_repo.findbygmail(userDetails.getUsername()).get();
	}
	
	@Override
	public ResponseEntity<file_reuqest> upload(MultipartFile fileones, String type)
		 {
			 Signgleton signgleton  = Signgleton.getInstance();
          name  = "";
		  Long iduser = user_authe().getId();
		  Path paths  = null;
		  if(type.equalsIgnoreCase("file")) {	  
			  // url + iduser + key + namefileconvert
			  String  namex =  iduser.toString() + "&3&" + fileones.getOriginalFilename();
			       name = convertfilename(signgleton.getUrlFile(), namex);
				   paths = Paths.get(signgleton.getUrlFile() + name);
				 id = savefile(name, type,fileones.getSize()).getId();
				 
		  } else if(type.equalsIgnoreCase("video")) {
			  String  namex =  iduser.toString() + "&3&" + fileones.getOriginalFilename();
			  name = convertfilename(signgleton.getUrlVideo(), namex);
				 paths = Paths.get(signgleton.getUrlVideo() + name);
				 id = savefile(name, type, fileones.getSize()).getId();
		  } else {
			  String  namex =  iduser.toString() + "&3&" + fileones.getOriginalFilename();		  
		       name = convertfilename(signgleton.getUrlImg(), namex);
			     paths = Paths.get(signgleton.getUrlImg() + name);
			     id = savefile(name, type, fileones.getSize()).getId();
		  }
		  try {
			Files.copy(fileones.getInputStream(),paths,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(200).body(file_reuqest.builder().namefile(name).type(type).id(id).build());
	}
	

	
	int i;
	public String convertfilename(String url, String name) {
		i = 0;
		File file  = new File(url + i +  "&3&" + name );
		while (file.exists() == true) {
			i++;
			file  = new File(url + i + "&3&" + name);
		}
		return   i + "&3&" + name;
	}


	
	public InputStream getresoure(String path, String filename) {
		String fullpath = path + File.separator + filename;
		try {
			InputStream is = new FileInputStream(fullpath);
			return is;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public ResponseEntity<datadetail_response> data_detail(String room) {
		List<filedetail_reponse> listimg = new ArrayList<filedetail_reponse>();
		List<filedetail_reponse> listvideo = new ArrayList<filedetail_reponse>();
		List<filedetail_reponse> listfile = new ArrayList<filedetail_reponse>();
		mess_repo.findAll().forEach((e) -> {
			if(e.getFriendmess() == null) {
				if(e.getGroupmess().getCoderoom().equalsIgnoreCase(room) == true) {
					apply(listimg,listvideo,listfile,e.getId());
				}
			}else {
				if(e.getFriendmess().getCoderoom().equalsIgnoreCase(room) == true) {
					apply(listimg,listvideo,listfile,e.getId());
				}	
			}
		});
		return ResponseEntity.ok(datadetail_response.builder().list_file(listfile).list_img(listimg).list_video(listvideo).build());
	}
	
	
	public void apply(List<filedetail_reponse> img, List<filedetail_reponse> video, List<filedetail_reponse> file, Long idmess) {
		file_repo.getallimg(idmess).ifPresent((e) -> {
			e.forEach((ex) -> {
				filedetail_reponse x = new filedetail_reponse();
				x.setId(ex.getId());
				x.setName(ex.getNamefile());
				x.setTime(null);
				if(ex.getTypefile().equalsIgnoreCase("file") == true) {
					x.setType(ex.getTypefile());
					file.add(x);
				}
				if(ex.getTypefile().equalsIgnoreCase("video") == true) {
					x.setType(ex.getTypefile());
					video.add(x);
				}
				if(ex.getTypefile().equalsIgnoreCase("image") == true) {
					x.setType(ex.getTypefile());
					img.add(x);
				}
			});
		});
	}

	String url = "";
	@Override
	public void deletefile(String namefile, String type) {
		 File file_one = new File("");
		Signgleton signgleton  = Signgleton.getInstance();
		if(type.equalsIgnoreCase("img") || type.equalsIgnoreCase("image")) {
			url  = signgleton.getUrlImg();
		}
		if(type.equalsIgnoreCase("video")) {
			url  = signgleton.getUrlVideo();
		}
		if(type.equalsIgnoreCase("file")) {
			url  = signgleton.getUrlFile();
		}
		Path paths = Paths.get(url + namefile);
		try {
			Files.deleteIfExists(paths);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ResponseEntity<List<filedetail_reponse>> data_detail_zoom(String room) {
		
		List<filedetail_reponse> data = new ArrayList<filedetail_reponse>();
		mess_repo.findAll().forEach((e) -> {
			if(e.getFriendmess() == null) {
				if(e.getGroupmess().getCoderoom().equalsIgnoreCase(room) == true) {
					file_repo.getallimg(e.getId()).get().forEach((ex) -> {
						if(!ex.getTypefile().equalsIgnoreCase("file")) {
							filedetail_reponse datax = new filedetail_reponse();
							datax.setName(ex.getNamefile());
							datax.setTime(e.getTime());
							datax.setType(ex.getTypefile());
							datax.setId(ex.getId());
							data.add(datax);	
						}
					});
				}
			} else {
				if(e.getFriendmess().getCoderoom().equalsIgnoreCase(room) == true) {
					file_repo.getallimg(e.getId()).get().forEach((ex) -> {
						if(!ex.getTypefile().equalsIgnoreCase("file")) {
							filedetail_reponse datax = new filedetail_reponse();
							datax.setName(ex.getNamefile());
							datax.setTime(e.getTime());
							datax.setType(ex.getTypefile());
							datax.setId(ex.getId());
							data.add(datax);	
						}
					});
				}	
			}
		});
		return ResponseEntity.ok(data);
	}
};
