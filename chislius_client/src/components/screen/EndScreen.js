import RoomNavbar from '../navbar/RoomNavbar'

function EndScreen({player, data}) {
    return (<>
        <RoomNavbar player={player} data={data}/>
        <h1>Игра завершена!</h1>
        <h2>Результаты:</h2>
        <ul>
            {data.players.map(otherPlayer => (<li key={otherPlayer.id}>
                {otherPlayer.name}: {otherPlayer.score} очков
                {otherPlayer.id === player.id && ' (Вы)'}
            </li>))}
        </ul>
        <button>В главное меню</button>
    </>)
}

export default EndScreen