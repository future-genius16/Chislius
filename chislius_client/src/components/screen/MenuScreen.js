function MenuScreen({userId, data}) {
    return (<>
        <h1>Главное меню</h1>
        <p>Ваш ID: {userId}</p>
        {data && <p>Ваш счет: {data.score}</p>}
        <button>Найти игру</button>
    </>)
}

export default MenuScreen