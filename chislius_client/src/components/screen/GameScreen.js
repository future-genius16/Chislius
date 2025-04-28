import React, {useState} from 'react'
import Board from '../board/Board'
import Form from '../form/Form'
import './GameScreen.css'

function GameScreen() {
    const [canSubmit, setCanSubmit] = useState(false)
    const [cardsList, setCardsList] = useState([])

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