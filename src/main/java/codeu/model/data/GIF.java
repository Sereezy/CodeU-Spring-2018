// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

/** Class representing a message. Messages are sent by a User in a Conversation. */
public class GIF extends Message {
  /**
   * Constructs a new GIF.
   *
   * @param id the ID of this GID
   * @param conversation the ID of the Conversation this GIF belongs to
   * @param author the ID of the User who sent this GIF
   * @param content the URL of this GIF
   * @param creation the creation time of this GIF
   */
  public GIF(UUID id, UUID conversation, UUID author, String content, Instant creation) {
	  super(id, conversation, author, content, creation);
  }

  
}
