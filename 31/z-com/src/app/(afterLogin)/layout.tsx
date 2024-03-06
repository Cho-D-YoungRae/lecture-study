export default function AfterLoginLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <div>
      After Login Layout
      {children}
    </div>
  );
}