import {Post} from "@/model/Post";

export interface PostImage {
  link: string;
  imageId: number;
  Post?: Post;
}