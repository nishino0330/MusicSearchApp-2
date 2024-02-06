// Spotify APIからカテゴリーを取得する関数
async function fetchCategories() {
    const response = await fetch('https://api.spotify.com/v1/browse/categories', {
        headers: {
            'Authorization': 'Bearer YOUR_ACCESS_TOKEN', // ここにアクセストークンをセット
        },
    });

    if (!response.ok) {
        console.error('Failed to fetch categories:', response.status, response.statusText);
        return [];
    }

    const data = await response.json();
    return data.categories.items;
}

// カテゴリーを取得して表示する関数
async function displayCategories() {
    const categories = await fetchCategories();

    // カテゴリーを表示するための処理
    const categoriesContainer = document.getElementById('categories-container');

    categories.forEach(category => {
        const categoryElement = document.createElement('p');
        categoryElement.textContent = category.name;
        categoriesContainer.appendChild(categoryElement);
    });
}

// ページが読み込まれたときにカテゴリーを表示
document.addEventListener('DOMContentLoaded', () => {
    displayCategories();
});
