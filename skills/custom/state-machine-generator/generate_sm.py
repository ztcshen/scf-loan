import click
import os
from jinja2 import Environment, FileSystemLoader

@click.command()
@click.option('--name', prompt='State Machine Name', help='Name of the state machine (e.g., OrderStateMachine)')
@click.option('--package', prompt='Package Name', help='Java package name')
@click.option('--states', prompt='States (comma separated)', help='List of states')
@click.option('--events', prompt='Events (comma separated)', help='List of events')
@click.option('--output', default='.', help='Output directory')
def generate(name, package, states, events, output):
    """Generates Java State Machine code."""
    
    script_dir = os.path.dirname(os.path.abspath(__file__))
    template_dir = os.path.join(script_dir, 'templates')
    env = Environment(loader=FileSystemLoader(template_dir))
    
    state_list = [s.strip() for s in states.split(',')]
    event_list = [e.strip() for e in events.split(',')]
    
    # Context for templates
    ctx = {
        'name': name,
        'package': package,
        'states': state_list,
        'events': event_list
    }
    
    # Ensure output dir exists
    package_path = package.replace('.', '/')
    full_output_path = os.path.join(output, package_path)
    os.makedirs(full_output_path, exist_ok=True)
    
    # Generate State Enum
    template = env.get_template('StateEnum.java.j2')
    with open(os.path.join(full_output_path, f"{name}State.java"), 'w') as f:
        f.write(template.render(ctx))
        
    # Generate Event Enum
    template = env.get_template('EventEnum.java.j2')
    with open(os.path.join(full_output_path, f"{name}Event.java"), 'w') as f:
        f.write(template.render(ctx))
        
    click.echo(f"State Machine code generated in {full_output_path}")

if __name__ == '__main__':
    generate()
