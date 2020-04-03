declare let google: any;

function draw() {

    var components = [
        {type: 'igoogle', datasource: 'https://spreadsheets.google.com/tq?key=pCQbetd-CptHnwJEfo8tALA',
            gadget: 'https://www.google.com/ig/modules/pie-chart.xml',
            userprefs: {'3d': 1}},
        {type: 'html', datasource: 'https://spreadsheets.google.com/tq?key=pCQbetd-CptHnwJEfo8tALA'},
        {type: 'csv', datasource: 'https://spreadsheets.google.com/tq?key=pCQbetd-CptHnwJEfo8tALA'},
        {type: 'htmlcode', datasource: 'https://spreadsheets.google.com/tq?key=pCQbetd-CptHnwJEfo8tALA',
            gadget: 'https://www.google.com/ig/modules/pie-chart.xml',
            userprefs: {'3d': 1},
            style: 'width: 800px; height: 700px; border: 3px solid purple;'}
    ];

    const container = document.getElementById('toolbar_div');
    google.visualization.drawToolbar(container as HTMLDivElement, components);
}

export function drawExportToolbar() {
    google.charts.setOnLoadCallback(draw);
}

