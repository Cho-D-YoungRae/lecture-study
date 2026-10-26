import {User} from "@/model/User";
import {PostImage} from "@/model/PostImage";

export interface Post {
  postId: number;
  User: User;
  content: string;
  createdAt: Date;
  Images: PostImage[];
}