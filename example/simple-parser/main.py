#!/usr/bin/python3

import sys

import yaml


def get_node_by_path(root: dict, path: str) -> dict:
    fs = path.split('/')

    node = root
    for name in fs:
        node = node[name]

    return node


def print_flow(root: dict, flow_name: str) -> None:
    flow_node = get_node_by_path(root, f'ci/flows/{flow_name}')

    visited = set()
    print('digraph {')

    for job_name in flow_node['jobs']:
        print_job(root, flow_name, job_name, visited)

    print('}')


def print_job(root: dict, flow_name: str, job_name: str, visited: set) -> None:
    job_node = get_node_by_path(root, f'ci/flows/{flow_name}/jobs/{job_name}')

    if 'needs' not in job_node:
        return

    needs = job_node['needs']
    if isinstance(needs, str):
        if (needs, job_name) in visited:
            return

        print(f'"{needs}" -> "{job_name}"')
        visited.add((needs, job_name))

        return

    needs_type = job_node['needs-type'] if 'needs-type' in job_node else None
    for need_node_name in needs:
        if (need_node_name, job_name) in visited:
            continue

        label = f'[label="{needs_type}"]' if needs_type else ''

        print(f'"{need_node_name}" -> "{job_name}"{label}')
        visited.add((need_node_name, job_name))

        print_job(root, flow_name, need_node_name, visited)


def main() -> None:
    file_name = sys.argv[1]
    flow = sys.argv[2]

    with open(file_name, 'rb') as f:
        root = yaml.safe_load(f)
    print_flow(root, flow)


if __name__ == '__main__':
    main()
