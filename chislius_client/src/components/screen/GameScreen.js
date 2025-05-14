import React, {useEffect, useState} from 'react'
import Board from '../board/Board'
import Form from '../form/Form'
import './GameScreen.css'
import Potions from "../potion/Potions";

function GameScreen({userId, roomId, data}) {
    const [canSubmit, setCanSubmit] = useState(false)
    const [cardsList, setCardsList] = useState([])
    const [potionsList, setPotionsList] = useState([])

    useEffect(() => {
        setPotionsList(data.potions.map((img, index) => ({
            id: index + 1, img: img
        })))
        if (cardsList.length === 0) {
            setCardsList(data.cards.map((initialValue, index) => {
                if (initialValue === null) {
                    return {
                        id: index + 1,
                        isOpen: false,
                        isDisabled: true,
                        img: 0
                    }
                } else if (initialValue === 0) {
                    return {
                        id: index + 1,
                        isOpen: false,
                        isDisabled: false,
                        img: 0
                    }
                } else {
                    return {
                        id: index + 1,
                        isOpen: true,
                        isDisabled: false,
                        img: initialValue
                    }
                }
            }))
        } else {
            setCardsList(cardsList.map((card, index) => {
                const newValue = data.cards[index];

                if (!card.isOpen) {
                    if (newValue === null) {
                        return {
                            ...card,
                            isDisabled: true
                        }
                    } else if (newValue === 0) {
                        return {
                            ...card,
                            isDisabled: false,
                        }
                    } else {
                        return {
                            ...card,
                            isOpen: true,
                            isDisabled: false,
                            img: newValue
                        }
                    }
                } else {
                    if (newValue === null) {
                        return {
                            ...card,
                            isOpen: false,
                            isDisabled: true,
                        }
                    } else if (newValue === 0) {
                        return {
                            ...card,
                            isOpen: false,
                            isDisabled: false,
                        }
                    } else {
                        return {
                            ...card,
                            img: newValue
                        }
                    }
                }
            }))
        }
        console.log(cardsList)
    }, [cardsList, data.cards, data.potions])

    const handleSubmit = () => {
    }

    const handleSkip = () => {
    }

    return (<div className='main'>
        <Potions potions={potionsList}/>
        <Board cards={cardsList}/>
        <Form
            onSubmit={handleSubmit}
            canSubmit={canSubmit}
            onSkip={handleSkip}
        />
    </div>)
}

export default GameScreen