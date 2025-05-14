function Potions({ potions }) {
    const handleClick = (clickedPotion) => {
        console.log(clickedPotion)
    }

    return (
        <section className="potions">
            <div className="potions__container">
                {potions.map((potion) => (
                    <Potion key={potion.id} potion={potion} onClick={handleClick} />
                ))}
            </div>
        </section>
    )
}

function Potion({ potion, onClick }) {
    const getPotionImage = () => {
        if (potion.img === null) {
            return (
                <img
                    src={`/images/potions/disabled.svg`}
                    alt={`Potion ${potion.img}`}
                    className="potion__image"
                />
            )
        }
        return (
            <img
                src={`/images/potions/${potion.img}.svg`}
                alt={`Potion ${potion.img}`}
                className="potion__image"
            />
        )
    }

    return (
        <article
            className={`potion${potion.img === null ? ' potion_invisible' : ''}`}
            onClick={() => onClick(potion)}
        >
            <div className={`potion__container${potion.isOpen ? ' potion_open' : ''}`}>
                <div className="potion__front">
                    <img
                        src={`/images/potions/0.svg`}
                        alt={`Potion ${potion.img}`}
                        className="potion__image"
                    />
                </div>
                <div className="potion__back">{getPotionImage()}</div>
            </div>
        </article>
    )
}

export default Potions