import React, {useEffect, useState} from 'react'
import Board from '../game/Board'
import Potions from '../game/Potions'
import {Button, Container} from 'react-bootstrap'
import api from '../../client/ApiClient'
import RoomNavbar from '../navbar/RoomNavbar'
import {States} from '../utils/States'
import {useAuth} from '../../context/TokenContext'
import Player from '../game/Player'

function GameScreen({player, state, data}) {
    const {token} = useAuth()

    const [cardsList, setCardsList] = useState([])
    const [potionsList, setPotionsList] = useState([])

    useEffect(() => {
        setPotionsList(data.board.potions.map((potion, index) => ({
            id: index + 1, ...potion
        })))
        if (cardsList.length === 0) {
            setCardsList(data.board.cards.map((initialValue, index) => {
                if (initialValue === null) {
                    return {
                        id: index + 1, isOpen: false, isDisabled: true, img: 0
                    }
                } else if (initialValue === 0) {
                    return {
                        id: index + 1, isOpen: false, isDisabled: false, img: 0
                    }
                } else {
                    return {
                        id: index + 1, isOpen: true, isDisabled: false, img: initialValue
                    }
                }
            }))
        } else {
            setCardsList(cardsList.map((card, index) => {
                const newValue = data.board.cards[index]

                if (!card.isOpen) {
                    if (newValue === null) {
                        return {
                            ...card, isDisabled: true
                        }
                    } else if (newValue === 0) {
                        return {
                            ...card, isDisabled: false
                        }
                    } else {
                        return {
                            ...card, isOpen: true, isDisabled: false, img: newValue
                        }
                    }
                } else {
                    if (newValue === null) {
                        return {
                            ...card, isOpen: false, isDisabled: true
                        }
                    } else if (newValue === 0) {
                        return {
                            ...card, isOpen: false, isDisabled: false
                        }
                    } else {
                        return {
                            ...card, img: newValue
                        }
                    }
                }
            }))
        }
    }, [data.board.cards, data.board.potions])

    const handleSubmit = () => {
        if (state !== States.MOVE) return

        api.submitMove(token).catch((err) => {
            alert(err.message)
        })
    }

    const handleSkip = () => {
        if (state !== States.MOVE) return

        api.skipMove(token).catch((err) => {
            alert(err.message)
        })
    }

    const onCardClick = (clickedCard) => {
        if (state !== States.MOVE) return

        api.flipCard(token, clickedCard.id).catch((err) => {
            alert(err.message)
        })
    }

    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container className={'mt-3'}>
            <div className="container text-center">
                <div className="row align-items-start">
                    <div className="col">
                        <Potions potions={potionsList}/>
                        <Board onClick={onCardClick} cards={cardsList}/>
                    </div>
                    <div className="col">
                        <h2>Игроки ждут хода:</h2>
                            {data.players.map(otherPlayer => (<><Player player={otherPlayer}/>
                                {otherPlayer.id === player.id && ' (Вы)'}<b> Счет: {otherPlayer.score}</b></>
                            ))}

                        <h2>Текущий игрок:</h2>
                        <Player
                            player={data.currentPlayer}/>{data.currentPlayer.id === player.id && ' (Вы)'}<b> Счет: {data.currentPlayer.score}</b>

                        {state === States.MOVE && <>
                            <Button size="lg" className="mt-3 mb-3 w-100" onClick={handleSubmit}>Сварить</Button>
                            <Button size="lg" className="mb-3 w-100" onClick={handleSkip}>Пропустить</Button>
                        </>}
                    </div>
                </div>
            </div>
        </Container>
    </>
)
}

export default GameScreen