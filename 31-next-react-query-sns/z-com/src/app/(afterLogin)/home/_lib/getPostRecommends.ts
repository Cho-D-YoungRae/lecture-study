export default async function getPostRecommends() {
  const res = await fetch('http://localhost:9090/api/postRecommends', {
    next: {
      tags: ['posts', 'recommends'],
    },
    cache: 'no-cache',
  });

  if (!res.ok) {
    throw new Error('Failed to fetch post recommends');
  }

  return res.json();
}