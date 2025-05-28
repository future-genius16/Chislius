import React, {useEffect, useState} from 'react'
import Board from '../board/Board'
import Form from '../form/Form'
import Potions from '../potion/Potions'
import {Container} from 'react-bootstrap'
import api from '../../client/ApiClient'
import RoomNavbar from '../navbar/RoomNavbar'
import {States} from '../room/States'

function GameScreen({player, state, data}) {
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

    const handleSubmit = async () => {
        if (state !== States.MOVE) return
        try {
            await api.submitMove()
        } catch (err) {
            alert(err.message)
        }
    }

    const handleSkip = async () => {
        if (state !== States.MOVE) return
        try {
            await api.skipMove()
        } catch (err) {
            alert(err.message)
        }
    }

    const onCardClick = async (clickedCard) => {
        if (state !== States.MOVE) return
        try {
            await api.flipCard(clickedCard.id)
        } catch (err) {
            alert(err.message)
        }
    }

    return (<>
        <RoomNavbar player={player} data={data}/>
        <Container>
            <h1>Комната {data.code}</h1>
            <h2>Игроки ждут хода:</h2>
            <ul>
                {data.players.map(otherPlayer => (<li key={otherPlayer.id}>
                    {otherPlayer.name}{otherPlayer.id === player.id && ' (Вы)'} Счет: {otherPlayer.score}
                </li>))}
            </ul>
            <h2>Текущий игрок:</h2>
            <b>{data.currentPlayer.name}{data.currentPlayer.id === player.id && ' (Вы)'} Счет: {data.currentPlayer.score}</b>
        </Container>
        <Potions potions={potionsList}/>
        <Board onClick={onCardClick} cards={cardsList}/>
        {state === 3 && <Form
            onSubmit={handleSubmit}
            canSubmit={() => true}
            onSkip={handleSkip}
        />}
    </>)
}

export default GameScreen