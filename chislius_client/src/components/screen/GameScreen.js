import React, {useCallback, useEffect, useState} from 'react'
import Board from '../board/Board'
import Form from '../form/Form'
import './GameScreen.css'

function GameScreen() {
    const [canSubmit, setCanSubmit] = useState(false)
    const [cardsList, setCardsList] = useState([])

    const refreshCards = useCallback(() => {
        setCardsList(prevCards => {
            if (prevCards.length === 0) return prevCards
            const newCards = [...prevCards]
            const randomIndex = Math.floor(Math.random() * newCards.length)
            newCards[randomIndex] = {
                ...newCards[randomIndex], isOpen: !newCards[randomIndex].isOpen
            }
            return newCards
        })
    }, [])

    useEffect(() => {
        let intervalId

        setCardsList(Array.from({length: 16}, (_, i) => ({
            id: i, isOpen: false
        })))

        refreshCards()
        intervalId = setInterval(refreshCards, 250)

        return () => {
            if (intervalId) clearInterval(intervalId)
        }
    }, [refreshCards])

    const clearBoard = () => {
        setCardsList(cardsList.map((item) => ({...item, isOpen: false})))
    }

    const handleSubmit = () => {
        clearBoard()
    }

    const handleSkip = () => {
        clearBoard()
    }

    return (<div className='main'>
        <Form
            onSubmit={handleSubmit}
            canSubmit={canSubmit}
            onSkip={handleSkip}
        />
        <Board cards={cardsList} setCards={setCardsList} setCanSubmit={setCanSubmit}/>
    </div>)
}

export default GameScreen