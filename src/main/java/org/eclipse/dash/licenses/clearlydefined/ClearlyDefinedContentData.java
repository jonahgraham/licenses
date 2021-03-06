/*************************************************************************
 * Copyright (c) 2019, The Eclipse Foundation and others.
 * 
 * This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License 2.0 which accompanies this 
 * distribution, and is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *************************************************************************/
package org.eclipse.dash.licenses.clearlydefined;

import javax.json.JsonObject;

import org.eclipse.dash.licenses.ContentId;
import org.eclipse.dash.licenses.IContentData;
import org.eclipse.dash.licenses.LicenseSupport.Status;

public class ClearlyDefinedContentData implements IContentData {

	/**
	 * This field holds data that presents a single unit of content answered by a
	 * query to the ClearlyDefined service. The value is in JSON format as in this
	 * (somewhat abridged) example:
	 * 
	 * <pre>
	 * 	{
	 *	    "_id": "npm/npmjs/-/write/1.0.3",
	 *	    "coordinates": {
	 *	        "name": "write", 
	 *	        "provider": "npmjs", 
	 *	        "revision": "1.0.3", 
	 *	        "type": "npm"
	 *	    }, 
	 *	    "licensed": {
	 *	        "declared": "MIT", 
	 *	        "facets": {
	 *	            "core": {
	 *	                "attribution": {
	 *	                    "parties": [
	 *	                        "Copyright (c) 2014-2017, Jon Schlinkert.", 
	 *	                        "Copyright (c) 2017, Jon Schlinkert (https://github.com/jonschlinkert)."
	 *	                    ], 
	 *	                    "unknown": 1
	 *	                }, 
	 *	                "discovered": {
	 *	                    "expressions": [
	 *	                        "MIT"
	 *	                    ], 
	 *	                    "unknown": 0
	 *	                }, 
	 *	                "files": 4
	 *	            }
	 *	        }, 
	 *	        "score": {
	 *	            "consistency": 15, 
	 *	            "declared": 30, 
	 *	            "discovered": 19, 
	 *	            "spdx": 15, 
	 *	            "texts": 15, 
	 *	            "total": 94
	 *	        }, 
	 *	        "toolScore": {
	 *	            "consistency": 15, 
	 *	            "declared": 30, 
	 *	            "discovered": 19, 
	 *	            "spdx": 15, 
	 *	            "texts": 15, 
	 *	            "total": 94
	 *	        }
	 *	    }, 
	 *	    "scores": {
	 *	        "effective": 97, 
	 *	        "tool": 97
	 *	    }
	 *	}
	 * </pre>
	 */
	private JsonObject data;
	private String id;
	private Status status;

	public ClearlyDefinedContentData(String id, JsonObject data) {
		this.id = id;
		this.data = data;
	}

	@Override
	public String getLicense() {
		// FIXME Should combine the declared and discovered licenses.
		return getDeclaredLicense();
	}

	public String getDeclaredLicense() {
		if (data.containsKey("licensed")) {
			JsonObject licensed = data.get("licensed").asJsonObject();
			if (licensed.containsKey("declared")) {
				return licensed.getString("declared");
			}
		}
		return null;
	}

	@Override
	public int getScore() {
		return getEffectiveScore();
	}

	public int getEffectiveScore() {
		return data.get("scores").asJsonObject().getInt("effective");
	}

	@Override
	public ContentId getId() {
		return ContentId.getContentId(id);
	}

	@Override
	public String getAuthority() {
		// Maybe return the Clearly Defined URL instead?
		return "clearlydefined";
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public Status getStatus() {
		return status;
	}
}
