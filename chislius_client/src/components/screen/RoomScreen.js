function RoomScreen({userId, roomId, data}) {
    return (<>
        <h1>Комната #{roomId}</h1>
        <h2>Игроки:</h2>
        <ul>
            {data?.players?.map(player => (<li key={player.id}>
                {player.name} {player.ready ? " Готов " : " Не готов "}
                {player.id === userId && " (Вы)"}
            </li>))}
        </ul>
        <button>Готов</button>
    </>)
}

export default RoomScreen