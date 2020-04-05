const TestData = {
    getHeaders: () => {
        return [
            { label: "Frage", key: "question" },
            { label: "Wert 1", key: "one" },
            { label: "Wert 2", key: "two" },
            { label: "Wert 3", key: "three" },
            { label: "Wert 4", key: "four" }
        ]
    },

    getDataArray: () => {
        return [
            ['City', '1', '2', '3', '4'],
            ['Question 1', 23, 47, 2, 5],
            ['Question 2', 24, 10, 40, 3],
            ['Question 3', 3, 57, 15, 1],
            ['Question 4', 67, 5, 3, 2],
            ['Question 5', 2, 5, 1, 69]
        ]
    },

    getDataLiteralObjArray: () => {
        return [
            { question: "City", one: 1, two: 2, three: 3, four: 4 },
            { question: "Question 1", one: 23, two: 47, three: 2, four: 5 },
            { question: "Question 2", one: 24, two: 10, three: 40, four: 3 },
            { question: "Question 3", one: 3, two: 57, three: 15, four: 1 },
            { question: "Question 4", one: 67, two: 5, three: 3, four: 2 },
            { question: "Question 5", one: 2, two: 5, three: 1, four: 69 }
        ]
    },

    getDataString: () => {
        return `City; 1; 2; 3; 4
Question 1; 23; 47; 2; 5
Question 2; 24; 10; 40; 3
Question 3; 3; 57; 15; 1
Question 4; 67; 5; 3; 2
Question 5; 2; 5; 1; 69
        `},

    getDataJson: () => {
        return [
            {
                "question": "City",
                "one": 1,
                "two": 2,
                "three": 3,
                "four": 4
            },
            {
                "question": "Question 1",
                "one": 23,
                "two": 47,
                "three": 2,
                "four": 5
            },
            {
                "question": "Question 2",
                "one": 24,
                "two": 10,
                "three": 40,
                "four": 3
            },
            {
                "question": "Question 3",
                "one": 3,
                "two": 57,
                "three": 15,
                "four": 1
            },
            {
                "question": "Question 4",
                "one": 67,
                "two": 5,
                "three": 3,
                "four": 2
            },
            {
                "question": "Question 5",
                "one": 2,
                "two": 5,
                "three": 1,
                "four": 69
            }
        ]
    }

};
export default TestData;
