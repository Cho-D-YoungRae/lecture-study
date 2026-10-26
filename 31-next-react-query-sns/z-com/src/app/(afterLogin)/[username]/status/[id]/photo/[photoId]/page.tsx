import Home from "@/app/(afterLogin)/home/page";

type Props = {
  params: { username: string, id: string, photoId: string }
}
export default function Page({ params }: Props) {
  params.username // elonmusk
  params.id // 1
  params.photoId // 1
  // 패러랠 라우팅, 인터셉팅 됨
  return (
    <Home/>
  );
}