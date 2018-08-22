package mp4;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The mp4 controller class for serving the reqeust.
 * @author domi
 *
 */
@RestController
public class Mp4Controller {
	private final static Logger log = LoggerFactory.getLogger(Mp4Controller.class);
	private static Map<String, Object> cacheVMap = new HashMap<>();
	private final static String cacheVMapKey_VMAP = "VMAP";
	
	
	/**
	 * Reload all videos from the mp4 media path.
	 * @return
	 */
	@GetMapping("/medias/reload")
	String reloadVideoList() {
		cacheVMap.remove(cacheVMapKey_VMAP);
		processVideoList();
		log.info("reloaded medias");
		return "reloaded";
	}

	/**
	 * Get the file list json string for all videos.
	 * @return
	 */
	@GetMapping("/medias")
	String getPlayListJson() {
		return processVideoList().get("playListJson");
	}

	/**
	 * Get the video link menu html content.
	 * @return
	 */
	@GetMapping("/medias/links")
	String getListOfFilesLink() {
		return processVideoList().get("videoLinkMenu");
	}
    
	/**
	 * Process the list of videos for links list and file list, etc.
	 * @return
	 */
	private Map<String, String> processVideoList() {
		Map<String, String> vMap = (Map<String, String>) cacheVMap.get(cacheVMapKey_VMAP);
		if(vMap != null){
			return vMap;
		}
		
		vMap = new HashMap<String, String>();
		StringBuffer sbf = new StringBuffer();
		StringBuffer sbfMenu = new StringBuffer();

		List<File> fileList = Mp4Helper.getListOfFilesForDir(Mp4Helper.getMediaPath(), Mp4Helper.getMediaTypeList());

		int videoCount = fileList.size();
		String reloadMedia = "[Reload-Media]";
		// add the play_all link at the beginning
		sbfMenu.append("<input id=\"reloadMedia\" type=\"button\" title=\"" + reloadMedia
				+ "\" class=\"btn-link\" href=\"#\" onclick=\"reloadMedia();\" value=\"" + reloadMedia + "\">");
		
		String playAllMenu = "Play-All-[" + videoCount + "]";
		// add the play_all link at the beginning
		sbfMenu.append("<input id=\"playAllMenu\" type=\"button\" title=\"" + playAllMenu
				+ "\" class=\"btn-link\" href=\"#\" onclick=\"viewAll();\" value=\"" + playAllMenu + "\">");

		String mediaPath = Mp4Helper.getMediaPath();

		// to populate list of url/names for playlist
		sbf.append("{\"playList\":[");
		for (int i = 0; i < videoCount; i++) {
			File f = fileList.get(i);
			String filePath = f.getAbsolutePath();
			String fileName = f.getName();
			// remove the mediaPath, replace the \ to /
			String url = filePath.replace(mediaPath, "").replace("\\", "/");

			// for link menu
			sbfMenu.append("<input type=\"button\" title=\"" + (i + 1)
					+ "\" class=\"btn-link\" href=\"#\" onclick=\"view('" + url + "', '" + fileName + "'," + i + ","
					+ videoCount + ");\" value=\"" + fileName + "\">");

			// for playListJson
			// append the , for 2nd+ entries
			if (!sbf.toString().endsWith("[")) {
				sbf.append(",");
			}
			sbf.append("{\"url\":\"" + url + "\",\"fileName\":\"" + fileName + "\"}");
		}
		sbf.append("]}");

		vMap.put("playListJson", sbf.toString());
		vMap.put("videoLinkMenu", sbfMenu.toString());

		//put into cache
		cacheVMap.put(cacheVMapKey_VMAP, vMap);
		log.info("loaded vmap into cache.");
		
		return vMap;
	}
	
}
