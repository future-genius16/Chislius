function EndScreen({userId, roomId, data}) {
    return (<>
        <h1>Игра завершена!</h1>
        <h2>Результаты:</h2>
        <ul>
            {data?.players?.map(player => (<li key={player.id}>
                {player.name}: {player.score} очков
                {player.winner && " 🏆"}
                {player.id === userId && " (Вы)"}
            </li>))}
        </ul>
        <button>В главное меню</button>
    </>)
}

export default EndScreen